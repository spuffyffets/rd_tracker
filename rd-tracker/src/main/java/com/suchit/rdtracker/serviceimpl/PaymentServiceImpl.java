package com.suchit.rdtracker.serviceimpl;

import com.suchit.rdtracker.config.AppConfig;
import com.suchit.rdtracker.dto.DashboardSummaryDTO;
import com.suchit.rdtracker.dto.PaidCustomerDTO;
import com.suchit.rdtracker.dto.PendingCustomerDTO;
import com.suchit.rdtracker.entity.Customer;
import com.suchit.rdtracker.entity.Payment;
import com.suchit.rdtracker.repository.CustomerRepository;
import com.suchit.rdtracker.repository.PaymentRepository;
import com.suchit.rdtracker.service.PaymentService;
import com.suchit.rdtracker.util.PdfReportUtil;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final CustomerRepository customerRepository;

	public PaymentServiceImpl(PaymentRepository paymentRepository, CustomerRepository customerRepository) {
		this.paymentRepository = paymentRepository;
		this.customerRepository = customerRepository;
	}

	@Override
	public Payment addPayment(Long customerId, Payment payment) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		if (paymentRepository.existsByCustomerAndPaymentMonth(customer, payment.getPaymentMonth())) {
			throw new RuntimeException("Payment already done for this month");
		}

		payment.setCustomer(customer);
		payment.setPaymentDate(LocalDateTime.now());

		return paymentRepository.save(payment);
	}

	@Override
	public List<Payment> getPaymentsByCustomer(Long customerId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		return paymentRepository.findByCustomer(customer);
	}

	@Override
	public boolean isPaymentDoneForMonth(Long customerId, YearMonth month) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		return paymentRepository.existsByCustomerAndPaymentMonth(customer, month);
	}

	@Override
	public List<PendingCustomerDTO> getPendingCustomersForMonth(YearMonth selectedMonth) {

		List<PendingCustomerDTO> result = new ArrayList<>();

		for (Customer customer : customerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))) {

			YearMonth current = AppConfig.APP_START_MONTH;

			YearMonth firstPendingMonth = null;
			int pendingCount = 0;

			while (!current.isAfter(selectedMonth)) {

				boolean paid = paymentRepository.existsByCustomerAndPaymentMonth(customer, current);

				if (!paid) {
					if (firstPendingMonth == null) {
						firstPendingMonth = current;
					}
					pendingCount++;
				}

				current = current.plusMonths(1);
			}

			if (pendingCount > 0) {
				PendingCustomerDTO dto = new PendingCustomerDTO();
				dto.setCustomerId(customer.getId());
				dto.setAccountNo(customer.getAccountNo());
				dto.setName(customer.getName());
				dto.setMonthlyAmount(customer.getMonthlyAmount());
				dto.setPendingSince(firstPendingMonth);
				dto.setPendingCount(pendingCount);

				result.add(dto);
			}
		}

		return result;
	}

	@Override
	public DashboardSummaryDTO getDashboardSummary(YearMonth month) {

		Double expected = customerRepository.getTotalExpectedAmount();
		Double collected = paymentRepository.getTotalCollectedForMonth(month);

		Double pending = expected - collected;

		return new DashboardSummaryDTO(expected, collected, pending);
	}

	@Override
	public List<PaidCustomerDTO> getPaidCustomersForMonth(YearMonth month) {

		List<Payment> payments = paymentRepository.findByPaymentMonth(month);

		List<PaidCustomerDTO> result = new ArrayList<>();

		for (Payment p : payments) {

			PaidCustomerDTO dto = new PaidCustomerDTO();
			dto.setCustomerId(p.getCustomer().getId());
			dto.setAccountNo(p.getCustomer().getAccountNo());
			dto.setName(p.getCustomer().getName());
			dto.setAmountPaid(p.getAmountPaid());
			dto.setPaymentMode(p.getPaymentMode());

			result.add(dto);
		}

		// ✅ A → Z sort
		result.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));

		return result;
	}

	@Override
	public byte[] generatePaidCustomersPdf(YearMonth month) {

		List<PaidCustomerDTO> paidCustomers = getPaidCustomersForMonth(month);

		try {
			return PdfReportUtil.generatePaidCustomersPdf(month, paidCustomers);
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate PDF");
		}
	}

	@Override
	public Payment updatePayment(Long paymentId, Payment updatedPayment) {

		Payment existing = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));

		// ❌ Do not allow duplicate month for same customer
		boolean alreadyExists = paymentRepository.existsByCustomerAndPaymentMonth(existing.getCustomer(),
				updatedPayment.getPaymentMonth());

		if (alreadyExists && !existing.getPaymentMonth().equals(updatedPayment.getPaymentMonth())) {
			throw new RuntimeException("Payment already exists for this month");
		}

		existing.setPaymentMonth(updatedPayment.getPaymentMonth());
		existing.setAmountPaid(updatedPayment.getAmountPaid());
		existing.setPaymentMode(updatedPayment.getPaymentMode());

		return paymentRepository.save(existing);
	}

	@Override
	public void deletePayment(Long paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));

		paymentRepository.delete(payment);
	}

	@Override
	public Payment getPaymentById(Long paymentId) {
		return paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
	}

}
